The API key is stored in gradle.properties. gradle.properties must be created in the following location.

Windows: C:\Users\<Your Username>\.gradle
Mac: /Users/<Your Username>/.gradle
Linux: /home/<Your Username>/.gradle 
  
Add the following line to gradle.properties:  
```
PopularMovies_ApiKey="yourapikeyfromthemoviedb"
```

If the API key does not import correctly, modify line 12 of build.gradle (app module) to the following:

```
buildConfigField 'String', "ApiKey", "whateveryourapikeyis"
```
