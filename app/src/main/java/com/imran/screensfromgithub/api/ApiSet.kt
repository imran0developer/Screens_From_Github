package com.imran.screensfromgithub.api

import com.imran.screensfromgithub.models.LayoutModel
import com.imran.screensfromgithub.models.XmlModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiSet {

    @GET("/repos/{username}/{repository}/contents/app/src/main/res/layout")
    suspend fun getAllLayoutFiles(
        @Path("username") username : String,
        @Path("repository") repository : String
    ) : Response<LayoutModel>

    @GET("repos/{username}/{repository}/git/blobs/{blob_sha}")
    suspend fun getXml(
        @Path("username") username: String,
        @Path("repository") repository: String,
        @Path("blob_sha") blobSha: String
    ): Response<XmlModel>



}