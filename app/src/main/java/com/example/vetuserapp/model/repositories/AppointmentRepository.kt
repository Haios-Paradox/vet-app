package com.example.vetuserapp.model.repositories

import android.graphics.Bitmap
import android.util.Log
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

object AppointmentRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    val appointRef = db.collection(References.APPOINT_COL)
    val doctorRef = db.collection(References.DOCTOR_COL)
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    val currentDate = Date()

    suspend fun getAppointment(id:String):Result<DocumentSnapshot>{
        return try{
            val result = appointRef.document(id).get().await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getUserQueue(appointmentId: String, doctorId: String): Result<Int> {
        Log.e("AppointmentRepo", "getUserQueue")
        try{
            val docDoctor = doctorRef.document(doctorId).get().await()
            val doctor = docDoctor.toObject<Doctor>()
            val appointments = appointRef.whereIn("id",doctor?.queue!!).get().await()
            val sortedAppointments = appointments.toObjects<Appointment>().sortedBy { it.timestamp }
            val result = getQueueNumber(sortedAppointments,appointmentId)
            return Result.success(result)
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    fun getQueueNumber(appointments: List<Appointment>, appointmentId: String) : Int {
        // find the appointment corresponding to the id
        val chosenAppointment = appointments.firstOrNull {it.id == appointmentId}
            ?: return -1

        // filter only appointments that come before the chosen one
        val previousAppointments = appointments.filter { it.timestamp != null && it.timestamp <= chosenAppointment.timestamp!! }

        return previousAppointments.size
    }

    suspend fun getAllUserAppointmentsHistory(
    ): Result<QuerySnapshot?> {
        return try{
            val uid = auth.uid ?: throw (Exception("User Not Logged In"))
            val result =  appointRef.whereEqualTo("patientId",uid).get().await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getDoctorQueue(
        doctorId : String?,
    ): Result<QuerySnapshot?> {
        return try{
            val result = appointRef.whereEqualTo("doctorId",doctorId).whereEqualTo("complete",false).get()
                .await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun createAppointment(
        user: User, doctor: Doctor, doctorId: String, image:Bitmap, desc:String
    ): Result<DocumentReference> {
        return try{
            val appointment = Appointment(
                null,
                auth.uid,
                user.name,
                doctorId,
                doctor.name,
                null,
                desc,
                null,
                currentDate.time,
                false
            )
            val result = appointRef.add(appointment).await()
            val avatarLink = sendImage(image,result.id)
            appointment.id = result.id
            appointment.photo = avatarLink
            appointRef.document(appointment.id!!).set(appointment).await()
            doctorRef.document(doctorId).update(
                "queue", FieldValue.arrayUnion(appointment.id)
            ).await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }

    }

    private suspend fun sendImage(image: Bitmap, appointmentId: String): String {
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        val time = currentDate.time.toString()
        return uploadImage(image,"$uid$time",appointmentId)
    }

    private suspend fun uploadImage(image: Bitmap, fileName: String, appointmentId: String): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val path = "UserData/Appointment/$appointmentId/$fileName.jpg"
        val imageRef = storageRef.child(path)
        return try {
            imageRef.putBytes(data).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            throw e
        }
    }

}