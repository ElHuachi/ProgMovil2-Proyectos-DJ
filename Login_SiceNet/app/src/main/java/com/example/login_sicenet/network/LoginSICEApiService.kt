package com.example.login_sicenet.network

import com.example.login_sicenet.model.Envelope
import com.example.login_sicenet.model.EnvelopeCalif
import com.example.login_sicenet.model.EnvelopeCalifUnidad
import com.example.login_sicenet.model.EnvelopeCargaAcademica
import com.example.login_sicenet.model.EnvelopeKardex
import com.example.login_sicenet.model.EnvelopeLogin
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
    fun login(@Body body: RequestBody): Call<EnvelopeLogin>

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/getAlumnoAcademicoWithLineamiento"
    )
    @POST("/ws/wsalumnos.asmx")
    fun getAcademicProfile(@Body body: RequestBody): Call<Envelope>

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/getAllCalifFinalByAlumnos"
    )
    @POST("/ws/wsalumnos.asmx")
    fun getCaliFinales(@Body body: RequestBody): Call<EnvelopeCalif>

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/getCalifUnidadesByAlumno"
    )
    @POST("/ws/wsalumnos.asmx")
    fun getCaliUnidades(@Body body: RequestBody): Call<EnvelopeCalifUnidad>

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/getAllKardexConPromedioByAlumno"
    )
    @POST("/ws/wsalumnos.asmx")
    fun getKardex(@Body body: RequestBody): Call<EnvelopeKardex>

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: http://tempuri.org/getCargaAcademicaByAlumno"
    )
    @POST("/ws/wsalumnos.asmx")
    fun getCargaAcademica(@Body body: RequestBody): Call<EnvelopeCargaAcademica>
}