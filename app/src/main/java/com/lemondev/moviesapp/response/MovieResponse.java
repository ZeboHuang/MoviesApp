package com.lemondev.moviesapp.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lemondev.moviesapp.models.MovieModel;

// this class is for single movie request.
public class MovieResponse {
    // 1 Find the movie object;

    /**
     * @SerializedName 将使用该注解的属性与 json 数据中的数据 映射
     * @Expose 决定是否将数据进行序列化(serialize) 或 反序列化(deserialize)
     *          https://blog.csdn.net/pzq915981048/article/details/97125214
     */
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie() {
        return movie;
    }
}
