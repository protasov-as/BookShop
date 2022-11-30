package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.dto.BookStatusDto;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/book")
public class BookshpCartController {

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "bookPostponedList")
    public List<Book> BookPostponedList() {
        return new ArrayList<>();
    }

    private final BookService bookService;

    @Autowired
    public BookshpCartController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            String[] cookieSlugs = createArrayWithBookSlugsFromCookie(cartContents);
            List<Book> cartBooksFromCookieSlugs = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", cartBooksFromCookieSlugs);
        }
        return "cart";
    }

    @GetMapping("/postponed")
    public String handlePostponedListRequest(@CookieValue(name = "postponedContents", required = false) String postponedContents,
                                             Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            model.addAttribute("isPostponedListEmpty", true);
        } else {
            model.addAttribute("isPostponedListEmpty", false);
            String[] postponedBooksFromCookieSlugs = createArrayWithBookSlugsFromCookie(postponedContents);
            List<Book> booksFromCookiesSlugs = bookService.findBooksBySlugIn(postponedBooksFromCookieSlugs);
            model.addAttribute("bookPostponedList", booksFromCookiesSlugs);
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name =
            "cartContents", required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/book");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/book/cart";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedListByRequest(@PathVariable("slug") String slug, Model model,
                                                             @CookieValue(name = "postponedContents", required = false)
                                                             String postponedContents, HttpServletResponse response) {
        if (postponedContents != null && !postponedContents.equals("")) {
            System.out.println(postponedContents);
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("postponedContents", String.join("/", cookieBooks));
            cookie.setPath("/book");
            response.addCookie(cookie);
            model.addAttribute("isPostponedListEmpty", false);
        } else {
            model.addAttribute("isPostponedListEmpty", true);
        }
        return "redirect:/book/postponed";
    }

    @PostMapping("/changeBookStatus/{slug}")
    @ResponseBody
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         @RequestBody BookStatusDto bookStatusDto,
                                         HttpServletResponse response,
                                         Model model) {
        String contents = bookStatusDto.getStatus().equals("KEPT") ? postponedContents : cartContents;
        String contentName = bookStatusDto.getStatus().equals("KEPT") ? "postponedContents" : "cartContents";
        String isContentEmpty = bookStatusDto.getStatus().equals("KEPT") ? "isPostponedEmpty" : "isCartEmpty";
        if (contents == null || contents.equals("")) {
            Cookie cookie = new Cookie(contentName, slug);
            cookie.setPath("/book");
//            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            model.addAttribute(isContentEmpty, false);
        } else if (!contents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(contents).add(slug);
            Cookie cookie = new Cookie(contentName, stringJoiner.toString());
            cookie.setPath("/book");
            response.addCookie(cookie);
            model.addAttribute(isContentEmpty, false);
        }
        return "redirect:/books/" + slug;
    }

    private String[] createArrayWithBookSlugsFromCookie(String contentsFromCookie) {
        contentsFromCookie = contentsFromCookie.startsWith("/") ? contentsFromCookie.substring(1) : contentsFromCookie;
        contentsFromCookie = contentsFromCookie.endsWith("/") ? contentsFromCookie.substring(0, contentsFromCookie.length() - 1)
                : contentsFromCookie;
        return contentsFromCookie.split("/");
    }
}
