package com.example.myapplication.api;

import com.example.myapplication.model.CastAndCrew;
import com.example.myapplication.model.ListMovies;
import com.example.myapplication.model.MoviesDetail;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Observable<ListMovies> getMoviesListPopular(@Query("page")int page);

    @GET("3/movie/top_rated?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Observable<ListMovies> getMoviesListTopRated(@Query("page")int page);

    @GET("3/movie/upcoming?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Observable<ListMovies> getMoviesListUpcoming(@Query("page")int page);

    @GET("3/movie/now_playing?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Observable<ListMovies> getMoviesListNowPlaying(@Query("page")int page);

    @GET("3/movie/{movieId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Observable<MoviesDetail> getMoviesDetail(@Path("movieId") int id);

    @GET("3/movie/{movieId}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Observable<CastAndCrew> getCastAndCrew(@Path("movieId") int id);
}
