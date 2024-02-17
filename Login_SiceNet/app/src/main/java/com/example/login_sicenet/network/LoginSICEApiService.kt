package com.example.login_sicenet.network

import com.example.login_sicenet.model.Envelope
import com.example.login_sicenet.model.GetAlumnoAcademicoWithLineamientoResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginSICEApiService {
    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/accesoLogin"
    )
    @POST("/ws/wsalumnos.asmx")
    fun login(@Body body: RequestBody): Call<ResponseBody>

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/getAlumnoAcademicoWithLineamiento"
    )
    @POST("/ws/wsalumnos.asmx")
    fun getAcademicProfile(@Body body: RequestBody): Call<Envelope>
}