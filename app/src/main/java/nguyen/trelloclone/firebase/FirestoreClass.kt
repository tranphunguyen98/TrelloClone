package nguyen.trelloclone.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import nguyen.trelloclone.activities.SignUpActivity
import nguyen.trelloclone.models.User
import nguyen.trelloclone.utils.Constants

class FirestoreClass {
    private val fireStore = FirebaseFirestore.getInstance()

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

    private fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}