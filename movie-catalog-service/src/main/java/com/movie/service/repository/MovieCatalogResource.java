package com.movie.service.repository;

import com.movie.service.models.CatalogItem;
import com.movie.service.models.UserRating;
import com.movie.service.models.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

       @Autowired
       RestTemplate restTemplate;

	
       @RequestMapping("/{userId}")
       public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
   
           UserRating userRating = restTemplate.getForObject("http://rating-data-service/ratingsdata/user/" + userId, 
                         UserRating.class);
   
           return userRating.getRatings().stream()
                   .map(rating -> {
                       Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                       return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                   })
                   .collect(Collectors.toList());
   
       }
}