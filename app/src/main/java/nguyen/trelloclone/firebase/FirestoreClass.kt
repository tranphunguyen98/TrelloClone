package nguyen.trelloclone.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import nguyen.trelloclone.activities.MainActivity
import nguyen.trelloclone.activities.SignInActivity
import nguyen.trelloclone.activities.SignUpActivity
import nguyen.trelloclone.models.User
import nguyen.trelloclone.utils.Constants

class FirestoreClass {
    private val fireStore = FirebaseFirestore.getInstance()

    fun signIn(activity: Activity) {
        fireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                val userLogged = document.toObject(User::class.java)
                if(userLogged != null) {
                    when(activity) {
                        is SignInActivity -> {
                            activity.signInSuccess(userLogged)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(userLogged)
                        }
                    }
                }
                else {
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document: userLogged null"
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        fireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }
}