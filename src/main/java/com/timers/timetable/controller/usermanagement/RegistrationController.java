package com.timers.timetable.controller.usermanagement;

import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.DeptService;
import com.timers.timetable.service.UserService;
import com.timers.timetable.statics.ParameterFiller;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private DeptService deptService;

    @GetMapping("/registration")
    public String registration(Model model) {

        // model.addAttribute("password2Error","Password confirmation error!");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {

        boolean isConfirmEmpty = ObjectUtils.isEmpty(passwordConfirm);

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Подтверждение пароля не может быть пустым");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            isConfirmEmpty = true;
            model.addAttribute("passwordError", "Не совпадает пароль и подтверждение!");
        }

        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("errorUserExists", "User exists");
            return "registration";
        }

        if (!user.getEmail().equals("")) {
            model.addAttribute("message", "На адрес " + user.getEmail() + " была отправлена \n" +
                    "ссылка для активации учетной записи");
        }

        return "login";
    }

    @GetMapping("/hello")
    public String hello(Model model) {

        ParameterFiller.fillModelParameters(model, userService, deptsRepo, deptService);
        return "hello";
    }


    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {

        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "Учетная запись успешно активирована!");
        } else {
            model.addAttribute("message", "Указанный код активации не найден!");
        }


        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {

        //model.addAttribute("message","message to login");
        return "login";
    }


    /**
     * Функции для восстановления / сброса пароля
     *
     * @param model
     * @return
     */

    @GetMapping("/restore")
    public String gotoRestore(Model model) {

        model.addAttribute("sendac", "sendActivationCode");
        return "registration";
    }


    @GetMapping("/restore/{code}")
    public String restorePassword(Model model, @PathVariable String code) {

        User userToResetPassw = userService.findByActivationCode(code);

        if (userToResetPassw != null) {
            model.addAttribute("resetpassword", "resetpassword");
            model.addAttribute("message", "Не найден код активации");
            // model.addAttribute("sendac");
            model.addAttribute("user", userToResetPassw);

        } else {
            model.addAttribute("message", "Не найден код активации");
            return "login";
        }

        return "registration";

    }

    @GetMapping("/sendac")
    public String sendActivationCode(Model model,
                                     @RequestParam Map<String, String> form) {

        String email;
        email = form.get("email");
        if (email == null) {
            model.addAttribute("message", "Email attribute not found at form parameters");
            return "login";
        }

        User userFindedByEmail = userService.findUserByEmail(email.toLowerCase());

        if (userFindedByEmail == null) {
            model.addAttribute("message", "По указанному email пользователь не найден");
            return "login";

        } else {
            model.addAttribute("message", "На адрес электронной почты \n"
                    + form.get("email").toLowerCase() + " была отправлена ссылка \n"
                    + "для восстановления доступа.");
            userService.sendActivationCode(userFindedByEmail);

            return "login";
        }

    }

    @PostMapping("/sendac")
    public String sendActivationCode1(Model model,
                                     @RequestParam Map<String, String> form) {

        String email;
        email = form.get("email");
        if (email == null) {
            model.addAttribute("message", "Email attribute not found at form parameters");
            return "login";
        }

        User userFindedByEmail = userService.findUserByEmail(email.toLowerCase());

        if (userFindedByEmail == null) {
            model.addAttribute("message", "По указанному email пользователь не найден");
            return "redirect:/user";

        } else {
            model.addAttribute("message", "На адрес электронной почты \n"
                    + form.get("email").toLowerCase() + " была отправлена ссылка \n"
                    + "для восстановления доступа.");
            userService.sendActivationCode(userFindedByEmail);

            return  "redirect:/user";
        }

    }
    @PostMapping("/logout")
    public String logout(){


        return "login";
    }

    @PostMapping("/resetpassword")
    public String resetPassword(@RequestParam Map<String, String> form,
                                Model model) {

        int i = 0;
        i++;

        String activationCode = form.get("activationCode");
        String password = form.get("password");
        String passwordConfirm = form.get("password2");

        User user = userService.findByActivationCode(activationCode);

        boolean isConfirmEmpty = ObjectUtils.isEmpty(passwordConfirm);

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Подтверждение пароля не может быть пустым");
        }

        if (password != null && !password.equals(passwordConfirm)) {
            isConfirmEmpty = true;
            model.addAttribute("passwordError", "Не совпадает пароль и подтверждение!");
        }

        if (isConfirmEmpty) { // || bindingResult.hasErrors()) {

            model.addAttribute("user", user);
            model.addAttribute("activationcode", activationCode);
            model.addAttribute("resetpassword", "resetpassword");
            return "registration";
        } else {

            user.setPassword(password);
            userService.simpleUpdateUser(user);

            model.addAttribute("message", "Пароль успешно обновлен");
            return "login";

        }


    }
}
