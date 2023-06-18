package com.socialnetwork.api.controller;

import com.socialnetwork.api.dto.authorized.UserDto;
import com.socialnetwork.api.exception.custom.AccessDeniedException;
import com.socialnetwork.api.exception.custom.NoUserWithSuchCredentialsException;
import com.socialnetwork.api.exception.custom.TokenInvalidException;
import com.socialnetwork.api.mapper.authorized.UserMapper;
import com.socialnetwork.api.models.additional.Response;
import com.socialnetwork.api.models.base.User;
import com.socialnetwork.api.security.CurrentUser;
import com.socialnetwork.api.security.jwt.JwtTokenUtil;
import com.socialnetwork.api.security.jwt.UserPrincipal;
import com.socialnetwork.api.service.NotificationService;
import com.socialnetwork.api.service.authorized.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.socialnetwork.api.util.Constants.Auth.AUTHORIZATION_HEADER;
import static com.socialnetwork.api.util.Constants.Auth.BEARER;
import static com.socialnetwork.api.util.Constants.Auth.CONFIRMATION_REQUIRED;
import static com.socialnetwork.api.util.Constants.Auth.WRONG_PASSWORD;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserMapper userMapper;
  private final NotificationService notificationService;

  @PostMapping()
  public ResponseEntity<?> logIn(@RequestBody UserDto.Request.Credentials userDto)
      throws NoUserWithSuchCredentialsException {
    User user = userService.findByEmailAddress(userDto.getEmailAddress());

    if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(WRONG_PASSWORD));
    }

    if (!user.isEnabled()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(CONFIRMATION_REQUIRED));
    }
    notificationService.saveLogin(user);

    return ResponseEntity.ok(userMapper.convertToAccountData(user,
          jwtTokenUtil.generateToken(user.getUsername(), userDto.getRememberMe())));
  }

  @GetMapping("jwt")
  public ResponseEntity<?> loginByToken(@CurrentUser UserPrincipal currentUser, HttpServletRequest request)
        throws NoUserWithSuchCredentialsException {
    String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (authHeader.startsWith(BEARER)) {
      authHeader = authHeader.substring(BEARER.length());
    }
    User user = userService.findByUsername(currentUser.getUsername());
    return ResponseEntity.ok(userMapper.convertToAccountData(user, authHeader));
  }

  @PostMapping("password-change")
  public ResponseEntity<?> passwordChange(@CurrentUser UserPrincipal currentUser,
                                          @RequestBody UserDto.Request.PasswordEditing passwords)
      throws NoUserWithSuchCredentialsException, AccessDeniedException {
    userService.passwordChange(currentUser.getUsername(), passwords.getOldPassword(), passwords. getNewPassword());
    return ResponseEntity.ok().build();
  }

  @GetMapping("recovery")
  public ResponseEntity<?> sendEmailForPasswordRecovery(@RequestParam("email") String email)
      throws NoUserWithSuchCredentialsException {
    userService.sendEmailForPasswordRecovery(email);
    return ResponseEntity.ok().build();
  }

  @PostMapping("recovery")
  public ResponseEntity<?> passwordRecovery(@RequestBody UserDto.Request.PasswordRecovery passwordRecovery)
      throws TokenInvalidException {
    userService.passwordRecovery(passwordRecovery.getToken(), passwordRecovery.getNewPassword());
    return ResponseEntity.ok().build();
  }
}