// Generated by view binder compiler. Do not edit!
package com.chatapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.chatapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView listOfConversation;

  @NonNull
  public final Button signOutButton;

  @NonNull
  public final TextView text;

  @NonNull
  public final TextView text1;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final ImageView userAvatar;

  @NonNull
  public final TextView usernameConnected;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView listOfConversation, @NonNull Button signOutButton,
      @NonNull TextView text, @NonNull TextView text1, @NonNull Toolbar toolbar,
      @NonNull ImageView userAvatar, @NonNull TextView usernameConnected) {
    this.rootView = rootView;
    this.listOfConversation = listOfConversation;
    this.signOutButton = signOutButton;
    this.text = text;
    this.text1 = text1;
    this.toolbar = toolbar;
    this.userAvatar = userAvatar;
    this.usernameConnected = usernameConnected;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.list_of_conversation;
      RecyclerView listOfConversation = ViewBindings.findChildViewById(rootView, id);
      if (listOfConversation == null) {
        break missingId;
      }

      id = R.id.sign_out_button;
      Button signOutButton = ViewBindings.findChildViewById(rootView, id);
      if (signOutButton == null) {
        break missingId;
      }

      id = R.id.text;
      TextView text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      id = R.id.text1;
      TextView text1 = ViewBindings.findChildViewById(rootView, id);
      if (text1 == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.user_avatar;
      ImageView userAvatar = ViewBindings.findChildViewById(rootView, id);
      if (userAvatar == null) {
        break missingId;
      }

      id = R.id.username_connected;
      TextView usernameConnected = ViewBindings.findChildViewById(rootView, id);
      if (usernameConnected == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, listOfConversation, signOutButton,
          text, text1, toolbar, userAvatar, usernameConnected);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
