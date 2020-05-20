package nguyen.trelloclone.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import nguyen.trelloclone.activities.*
import nguyen.trelloclone.models.Board
import nguyen.trelloclone.models.User
import nguyen.trelloclone.utils.Constants

class FirestoreClass {
    private val fireStore = FirebaseFirestore.getInstance()

    fun getBoardsList(activity: MainActivity) {

        fireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val boardsList: ArrayList<Board> = ArrayList()

                for (i in document.documents) {

                    val board = i.toObject(Board::class.java)!!
                   // board.documentId = i.id

                    boardsList.add(board)
                }

                Log.e("boardList", boardsList.size.toString())
                // Here pass the result to the base activity.
               // activity.populateBoardsListToUI(boardsList)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board) {
        fireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                activity.createBoardSuccessful()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun updateUserProfileData(activity:MyProfileActivity, userHashMap: HashMap<String, Any>) {
        fireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Updated successfully!", Toast.LENGTH_LONG).show()

                activity.profileUpdateSuccess()
            }.addOnFailureListener {e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.",
                    e
                )
            }
    }

    fun loadUserData(activity: Activity) {
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
                        is MyProfileActivity -> {
                            activity.setDataUserOnUI(userLogged)
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