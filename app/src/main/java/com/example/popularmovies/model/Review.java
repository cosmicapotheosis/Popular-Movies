package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Review {

    //    {
//        "id": 324857,
//        "page": 1,
//        "results": [
//            {
//                "author": "trineo03",
//                    "content": "First of all, I love the animation style in this film. The animation in this film is styled to look like an actual comic book. I think this approach for this kind of film was an excellent choice because we’ve all seen the usual kind of animation but nothing like this. To tie in with the animation I need to talk about the action scenes in this film. These tie in with the animation because of the way the directors styled the action shots is to look like something in a comic panel. Most people don’t know who Miles Morales is and after this film, you’ll want to learn more about him because of how they made him so relatable in this film. He acts like a typical teen in this situation compared to other versions of Spider-Man. Other versions kind of just acted like they always knew how to use their powers when Miles struggles with his. Miles isn’t the only relatable character film all of them are. The creators of this film did a “Marvel”ous job at making each character in this film somebody at least one person in the audience can relate to. All of the voice actors did a great job in their respected roles but it would’ve been nice to have a returning voice to at least one of the Spider-Man. It would’ve been cool to hear a returning voice even if it was for a few seconds. I have to talk about the humour in this film. It isn’t overpowered in this film and I felt like it had just the right amount of humour that will make everybody laugh. They poke fun of things that wouldn’t make sense in a real movie and other Spider-Man movies. Not a single moment in this film felt rushed or slowed down every scene felt the right pace for a movie like this. The cinematography in this movie was spectacular. This is probably because of it looking like a comic book and how the lighting needs to match up with how it would look in an actual book. If you are a comic book junkie you’ll love all of the easter eggs in this film. Some of them just comic book readers will get but others a majority of people will understand. The music in this film is fantastic. Every song in this film isn’t overused and matches perfectly with the age of the character. Somebody Miles age would be listening to the type of music he listens to. And the music without lyrics helps increase the emotion in the film. Also, there are two end credit scenes that are worth waiting for. In the end, this film is perfect for everybody. I give Spider-Man: Into the Spider-Verse a 10/10.",
//                    "id": "5c17088b92514132ba0be321",
//                    "url": "https://www.themoviedb.org/review/5c17088b92514132ba0be321"
//            },
//            {
//                "author": "Gimly",
//                    "content": "It's true I liked it less than perhaps the vast majority of _Spider-Verse's_ audience, but this was still great, the animation enamouring, and the depth of its story and reference totally engaging. Not to me the best Spider-Man movie as many have said, (that honour still goes to _Homecoming_) but a blast all the same.\r\n\r\n_Final rating:★★★½ - I really liked it. Would strongly recommend you give it your time._",
//                    "id": "5c6b7a529251412fc40c2bb0",
//                    "url": "https://www.themoviedb.org/review/5c6b7a529251412fc40c2bb0"
//            }
//        ],
//        "total_pages": 1,
//        "total_results": 2
//    }

    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("content")
    private String content;
    @SerializedName("author")
    private String author;

    public Review(String id, String url, String content, String author) {
        this.id = id;
        this.url = url;
        this.content = content;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() { return author; }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) {
        this.author = author;
    }

}
