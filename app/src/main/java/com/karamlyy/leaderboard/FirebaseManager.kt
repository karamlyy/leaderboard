package com.karamlyy.leaderboard

import com.google.android.material.floatingactionbutton.FloatingActionButton.Size
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class User(
    val id: String,
    val email: String,
    val username: String,
    val userType: UserType,
    val school: String,
)

data class LeaderboardMember(
    val username: String,
    val point: Long

)

data class Rating(
    val id: String,
    val admin: String,
    val point: Long,
    val comment: String
)

enum class UserType {
    STUDENT, ADMIN
}

class FirebaseManager {
    companion object {
        val instance: FirebaseManager = FirebaseManager()
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var user: User? = null
    suspend fun login(email: String, password: String) {
        val document = firestore.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()
            .first()

        this.user = User(
            id = document.id,
            email = email,
            username = document.get("username") as String,
            userType = UserType.valueOf(
                document.get("user_type") as String
            ),
            school = document.get("school") as String
        )
    }

    fun logout() {
        user = null
    }

    suspend fun submit(email: String, point: Int, comment: String) {
        val document = firestore.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo("user_type", "STUDENT")
            .get()
            .await()
            .first()

        firestore.collection("student_rating").add(
            hashMapOf(
                "user_id" to document.id,
                "admin_id" to getUser().id,
                "point" to point,
                "comment" to comment
            )
        ).await()
    }

    // don't call this before successful login
    fun getUser(): User {
        return user!!
    }

    suspend fun getTotalPoint(): Long {
        val documents = firestore.collection("student_rating")
            .whereEqualTo("user_id", getUser().id)
            .get()
            .await()
            .documents

        var point: Long = 0

        documents.forEach {
            point += it.get("point") as Long
        }

        return point
    }
    suspend fun getPoints(): List<Rating> {
        val documents = firestore.collection("student_rating")
            .whereEqualTo("user_id", getUser().id)
            .get()
            .await()
            .documents

        val ratings = mutableListOf<Rating>()

        documents.forEach {
            val admin_username = firestore.collection("user")
                .get()
                .await()
                .first { user ->
                    user.id == (it.get("admin_id") as String)
                }
                .get("username") as String

            ratings.add(
                Rating(
                    id = it.id,
                    admin = admin_username,
                    point = it.get("point") as Long,
                    comment = it.get("comment") as String
                )
            )
        }
        return ratings
    }

    suspend fun getLeaderboard(): List<LeaderboardMember> {
        val documents = firestore.collection("user")
            .get()
            .await()
            .documents
            .filter {
                UserType.valueOf(
                    it.get("user_type") as String
                ) == UserType.STUDENT
            }

        val members = mutableListOf<LeaderboardMember>()

        documents.forEach {  doc ->
            var point: Long = 0

            val ratings = firestore.collection("student_rating")
                .whereEqualTo("user_id", doc.id)
                .get()
                .await()
                .documents

            ratings.forEach {
                point += it.get("point") as Long
            }

            members.add(
                LeaderboardMember(
                    username = doc.get("username") as String,
                    point = point
                )
            )
        }

        return members.sortedByDescending { it.point }
    }

}