package com.socialnetwork.api.service;

import com.socialnetwork.api.entity.ConfirmationToken;
import com.socialnetwork.api.entity.User;
import com.socialnetwork.api.exception.EmailVerificationException;
import com.socialnetwork.api.repository.ConfirmationTokenRepository;
import com.socialnetwork.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final EmailService emailService;
  private static final String CONFIRM_ACCOUNT_URL = "http://localhost:8080/account/registration/confirm-account?token=";

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findByEmailAddress(String emailAddress) {
    return userRepository.findByEmailAddress(emailAddress);
  }

  public void saveUser(User user) {
    userRepository.save(user);

    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    confirmationTokenRepository.save(confirmationToken);

    emailService.sendEmail(user, confirmationToken, CONFIRM_ACCOUNT_URL);

    System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());
  }

  public void confirmEmail(String confirmationToken) throws EmailVerificationException {
    Optional<ConfirmationToken> optionalToken =
            confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (optionalToken.isEmpty()) {
      throw new EmailVerificationException("Error: Couldn't verify email");
    }

    ConfirmationToken token = optionalToken.get();
    User user = findByEmailAddress(token.getUser().getEmailAddress()).get();
    user.setEnabled(true);
    userRepository.save(user);
  }
}
