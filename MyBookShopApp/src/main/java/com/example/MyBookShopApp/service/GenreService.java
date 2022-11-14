package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.GenreRepository;
import com.example.MyBookShopApp.dto.GenreDto;
import com.example.MyBookShopApp.struct.genre.GenreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> getAllGenresStructure() {
        List<GenreEntity> firstTierGenreList = getFirstTierGenres();
        List<GenreEntity> secondTierGenresList = new ArrayList<>();
        List<GenreEntity> thirdTierGenresList = new ArrayList<>();
        firstTierGenreList.forEach(g -> {
            List<GenreEntity> secondTierGenresListTemp = getByParentGenreId(g.getId());
            if(!secondTierGenresListTemp.isEmpty()) {
                secondTierGenresList.addAll(secondTierGenresListTemp);
                secondTierGenresList.forEach(gg -> {
                    List<GenreEntity> thirdTierGenresListTemp = getByParentGenreId(gg.getId());
                    if(!thirdTierGenresListTemp.isEmpty()) {
                        thirdTierGenresList.addAll(thirdTierGenresListTemp);
                    }
                });
            }
        });
        firstTierGenreList.addAll(secondTierGenresList);
        firstTierGenreList.addAll(thirdTierGenresList);
        return firstTierGenreList;
    }

    public List<GenreDto> getAllGenresStructureDto() {
        List<GenreDto> firstTierGenreDtoList = convertListGenreToGenreDto(getFirstTierGenres());
        firstTierGenreDtoList.forEach(genreDtoTierOne -> {
            List<GenreDto> secondTierGenreDtoList = new ArrayList<>();
            getByParentGenreId(genreDtoTierOne.getGenreEntity().getId()).forEach(secondTierGenre -> {
                GenreDto genreDtoTierTwo  = new GenreDto();
                genreDtoTierTwo.setGenreEntity(secondTierGenre);
                genreDtoTierTwo.setCount(genreRepository.countBooksByGenreId(secondTierGenre.getId()));

                List<GenreDto> thirdTierGenreDtoList = new ArrayList<>();
                getByParentGenreId(secondTierGenre.getId()).forEach(thirdTierGenre -> {
                            GenreDto genreDtoTierThree  = new GenreDto();
                            genreDtoTierThree.setGenreEntity(thirdTierGenre);
                            genreDtoTierThree.setCount(genreRepository.countBooksByGenreId(thirdTierGenre.getId()));
                            genreDtoTierTwo.setCount(genreDtoTierTwo.getCount() + genreDtoTierThree.getCount());
                            thirdTierGenreDtoList.add(genreDtoTierThree);
                        });

                genreDtoTierTwo.setSubGenres(thirdTierGenreDtoList);
                genreDtoTierOne.setCount(genreDtoTierOne.getCount() + genreDtoTierTwo.getCount());
                secondTierGenreDtoList.add(genreDtoTierTwo);
            });
            genreDtoTierOne.setSubGenres(secondTierGenreDtoList);
        });
        return firstTierGenreDtoList;
    }

    public List<GenreDto> convertListGenreToGenreDto(List<GenreEntity> genreEntityList) {
        List<GenreDto> genreDtoList = new ArrayList<>();
        genreEntityList.forEach(g -> {
            GenreDto genreDto  = new GenreDto();
            genreDto.setGenreEntity(g);
            genreDto.setCount(genreRepository.countBooksByGenreId(g.getId()));
            genreDtoList.add(genreDto);
        });
        return genreDtoList;
    }

    public List<GenreEntity> getFirstTierGenres() {
        return genreRepository.findByParentIdIsNull();
    }

    public List<GenreEntity> getByParentGenreId(Integer parentGenreId) {
        return genreRepository.findByParentIdIs(parentGenreId);
    }

    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<GenreEntity> findById(Integer id) {
        return genreRepository.findById(id);
    }

    public Optional<GenreEntity> findGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }

    public List<GenreEntity> findGenreWithSubgenresBySlug(String slug) {
        ArrayList<GenreEntity> genreList = new ArrayList<>();
        Optional<GenreEntity> genre = genreRepository.findBySlug(slug);
        if(genre.isPresent() && !getByParentGenreId(genre.get().getId()).isEmpty()) {
            getByParentGenreId(genre.get().getId()).forEach(g -> {
                if(!getByParentGenreId(g.getId()).isEmpty()){
                    genreList.addAll(getByParentGenreId(g.getId()));
                }
                genreList.add(g);
            });
        }
        genreList.add(genre.get());
        return genreList;
    }
}
