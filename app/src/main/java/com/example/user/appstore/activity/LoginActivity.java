package com.example.user.appstore.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.appstore.model.UserModel;
import com.example.user.appstore.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;

import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.*;


public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {


    private static final String TAG = "LoginActivity";
    //google
    private GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 777;
    private GoogleSignInClient mGoogleSignInClient;

    //facebook
    private LoginButton loginbtn;
    private CallbackManager callbackManager;
    private Button buttonFacebookLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//activity_login
        FacebookSdk.sdkInitialize(getApplicationContext());

        buttonFacebookLogin = findViewById(R.id.buttonLoginFacebook);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            openMainActivity();
            finish();
            return;
        }


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( @NonNull FirebaseAuth firebaseAuth ) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                   openMainActivity();
                }
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = getClient(this, gso);
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_google);


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed( @NonNull ConnectionResult connectionResult ) {
                        Toast.makeText(LoginActivity.this, "you got an error", Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //google
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                signIn();
            }

        });

        //facebook
        buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                loginWithFacebook();
            }
        });

    }


    private void loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onSuccess( LoginResult loginResult ) {
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Log.e(TAG, "facebook login cancel");
    }

    @Override
    public void onError( FacebookException error ) {
        Log.e(TAG, "facebook login error : " + error.getLocalizedMessage());
    }

    private void handleFacebookAccessToken( AccessToken token ) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( @NonNull Task<AuthResult> task ) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "facebook:onError", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUserIfNeeded(user.getUid(), user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString(), "user");
                           openMainActivity();
                        }

                    }
                });
    }

    //google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //google,facebook


    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // firebaseAuthWithGoogle(account);
                handleSignInResult(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //google
    private void handleSignInResult( GoogleSignInAccount account ) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "google:onError", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    writeNewUserIfNeeded(user.getUid(),
                            user.getEmail(),
                            user.getDisplayName(),
                            user.getPhotoUrl().toString(), "user");

                    openMainActivity();
                }
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void writeNewUserIfNeeded( final String userId, final String email, final String name, final String profileUrl, final String level ) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        final DatabaseReference usersRef = mDatabase.child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                if (!dataSnapshot.exists()) {
                    UserModel userData = new UserModel(userId, email, name, profileUrl, level);
                    usersRef.setValue(userData);
                    // userData.
                }

            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });
    }

}
