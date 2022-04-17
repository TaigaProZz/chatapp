// Generated by view binder compiler. Do not edit!
package com.chatapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.chatapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView backArrow;

  @NonNull
  public final EditText passwordRegister;

  @NonNull
  public final Button registerButton;

  @NonNull
  public final EditText usernameRegister;

  private ActivityRegisterBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView backArrow,
      @NonNull EditText passwordRegister, @NonNull Button registerButton,
      @NonNull EditText usernameRegister) {
    this.rootView = rootView;
    this.backArrow = backArrow;
    this.passwordRegister = passwordRegister;
    this.registerButton = registerButton;
    this.usernameRegister = usernameRegister;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backArrow;
      ImageView backArrow = ViewBindings.findChildViewById(rootView, id);
      if (backArrow == null) {
        break missingId;
      }

      id = R.id.password_register;
      EditText passwordRegister = ViewBindings.findChildViewById(rootView, id);
      if (passwordRegister == null) {
        break missingId;
      }

      id = R.id.register_button;
      Button registerButton = ViewBindings.findChildViewById(rootView, id);
      if (registerButton == null) {
        break missingId;
      }

      id = R.id.username_register;
      EditText usernameRegister = ViewBindings.findChildViewById(rootView, id);
      if (usernameRegister == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((ConstraintLayout) rootView, backArrow, passwordRegister,
          registerButton, usernameRegister);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
