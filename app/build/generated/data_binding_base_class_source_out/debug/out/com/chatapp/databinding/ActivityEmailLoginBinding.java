// Generated by view binder compiler. Do not edit!
package com.chatapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.chatapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEmailLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView gotoRegisterButton;

  @NonNull
  public final Button loginButton;

  @NonNull
  public final EditText passwordLogin;

  @NonNull
  public final EditText usernameLogin;

  private ActivityEmailLoginBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView gotoRegisterButton, @NonNull Button loginButton,
      @NonNull EditText passwordLogin, @NonNull EditText usernameLogin) {
    this.rootView = rootView;
    this.gotoRegisterButton = gotoRegisterButton;
    this.loginButton = loginButton;
    this.passwordLogin = passwordLogin;
    this.usernameLogin = usernameLogin;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEmailLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEmailLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_email_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEmailLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.goto_register_button;
      TextView gotoRegisterButton = ViewBindings.findChildViewById(rootView, id);
      if (gotoRegisterButton == null) {
        break missingId;
      }

      id = R.id.login_button;
      Button loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.password_login;
      EditText passwordLogin = ViewBindings.findChildViewById(rootView, id);
      if (passwordLogin == null) {
        break missingId;
      }

      id = R.id.username_login;
      EditText usernameLogin = ViewBindings.findChildViewById(rootView, id);
      if (usernameLogin == null) {
        break missingId;
      }

      return new ActivityEmailLoginBinding((ConstraintLayout) rootView, gotoRegisterButton,
          loginButton, passwordLogin, usernameLogin);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
