// Generated by view binder compiler. Do not edit!
package com.chatapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.chatapp.R;
import com.google.android.gms.common.SignInButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView backArrow;

  @NonNull
  public final SignInButton googleButton;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final Button loginEmailButton;

  @NonNull
  public final Button registerEmailButton;

  private ActivityLoginMainBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView backArrow,
      @NonNull SignInButton googleButton, @NonNull ImageView imageView,
      @NonNull Button loginEmailButton, @NonNull Button registerEmailButton) {
    this.rootView = rootView;
    this.backArrow = backArrow;
    this.googleButton = googleButton;
    this.imageView = imageView;
    this.loginEmailButton = loginEmailButton;
    this.registerEmailButton = registerEmailButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backArrow;
      ImageView backArrow = ViewBindings.findChildViewById(rootView, id);
      if (backArrow == null) {
        break missingId;
      }

      id = R.id.google_button;
      SignInButton googleButton = ViewBindings.findChildViewById(rootView, id);
      if (googleButton == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.login_email_button;
      Button loginEmailButton = ViewBindings.findChildViewById(rootView, id);
      if (loginEmailButton == null) {
        break missingId;
      }

      id = R.id.register_email_button;
      Button registerEmailButton = ViewBindings.findChildViewById(rootView, id);
      if (registerEmailButton == null) {
        break missingId;
      }

      return new ActivityLoginMainBinding((ConstraintLayout) rootView, backArrow, googleButton,
          imageView, loginEmailButton, registerEmailButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
