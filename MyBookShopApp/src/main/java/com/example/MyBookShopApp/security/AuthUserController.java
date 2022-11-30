package com.example.MyBookShopApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister userRegister;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister, BookstoreUserDetailsService bookstoreUserDetailsService) {
        this.userRegister = userRegister;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        model.addAttribute("curUsr", bookstoreUserDetailsService.getCurrentUser());
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", bookstoreUserDetailsService.getCurrentUser());
        return "profile";
    }

    @Autowired
    OAuth2AuthorizedClientService authclientService;

    @RequestMapping("/signin/oauth2LoginSuccess")
    public String getOauth2LoginInfo(Model model,@AuthenticationPrincipal OAuth2AuthenticationToken authenticationToken) {
        // fetching the client details and user details
        System.out.println(authenticationToken.getAuthorizedClientRegistrationId()); // client name like facebook, google etc.
        System.out.println(authenticationToken.getName()); // facebook/google userId

        //		1.Fetching User Info
        OAuth2User user = authenticationToken.getPrincipal(); // When you login with OAuth it gives you OAuth2User else UserDetails
        System.out.println("userId: "+user.getName()); // returns the userId of facebook something like 12312312313212
        // getAttributes map Contains User details like name, email etc// print the whole map for more details
        System.out.println("Email:"+ user.getAttributes().get("email"));

        //2. Just in case if you want to obtain User's auth token value, refresh token, expiry date etc you can use below snippet
        OAuth2AuthorizedClient client = authclientService
                .loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(),
                        authenticationToken.getName());
        System.out.println("Token Value"+ client.getAccessToken().getTokenValue());

        //3. Now you have full control on users data.You can eitehr see if user is not present in Database then store it and
        // send welcome email for the first time
        model.addAttribute("name", user.getAttribute("name"));
        return "my";
    }

//    @GetMapping("/logout")
//    public String handleLogout(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if (session != null) {
//            session.invalidate();
//        }
//
//        for (Cookie cookie : request.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//
//        return "redirect:/";
//    }
}
