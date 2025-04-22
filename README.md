# Telusko-Spring-Security
[Link To Video](https://www.youtube.com/watch?v=oeni_9g7too)

- Spring Security can be activated by adding the spring security dependency,
- By default it the user name is user and a default password is given by spring security on each session which can be seen in the console.
- If you want to change the default username and password then add these values in `application.properties` file: 
    ```
      spring.security.user.name=<your username> 
      spring.security.user.password=<your password>
    ```
  
## CSRF: Cross Site Request Forgery
CSRF stands for Cross-Site Request Forgery. It's a type of web security vulnerability where a malicious website tricks a user's browser into performing actions on another website where the user is authenticated (logged in).

### How CSRF Works (Simple Flow):
1. You're logged into Site A (e.g., your bank).

2. You visit Malicious Site B while still logged into Site A.

3. Site B sends a hidden request (like a fund transfer or profile update) to Site A using your cookies/session.

4. Since you're logged in, Site A trusts the request and processes it.

5. You just unknowingly performed an action on Site A.

### How to Prevent CSRF
- CSRF Tokens: Unique, random tokens tied to user sessions. Must be included in every form submission or state-changing request.

- SameSite Cookies: Setting `SameSite=Strict` or `SameSite=Lax` prevents cookies from being sent on cross-origin requests.

- Double Submit Cookies: Send a token in both a cookie and request body/header, and compare on the server.

- Re-authentication for sensitive actions.

Note: When we use spring security, it allows us to perform the get request with just username and password. But it do not allow the state changing request like POST or PUT or DELETE, without the CSRF token.
So, we will know how to get the CSRF token.

### Getting CSRF Token
- The `HttpServletRequest` object `getAttribute` methods with input parameter
`_csrf` can be used to get the csrf token for current loggedin user.

See Code:
```
 @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute("_csrf");
    }
```

See Response:
```agsl
{
    "parameterName": "_csrf",
    "token": "yE53IgH_It-mBbC6lJVR1U3bDbXUWWBG7MusGPlfAFwx3j89-nsSEjjOF7uLM4mDobhltCzpINfjaAVrjv2bKp05ZmoB6QkE",
    "headerName": "X-CSRF-TOKEN"
}
```

Now, when making `POST`, `PUT` and `DELETE` request, pass this
`CSRF` token in the header with header name `X-CSRF-TOKEN` and it will work fine.

### Configuring or Customizing the Default filters of Spring Security 
Till now, we were using the default filters of the spring security.
Now, we can customize them as per our requirement. 

For this, we should first create a `config` class where we will define beans

1. The first step is to create prevent the default flow of spring security by using 
    `@EnableWebSecurity` annotation. This will make the application to run through our secuirty flow which we have defined
  in the config class.
2. `@Configuration` annotation tells spring container that it is a configuration class.
3. We get the object of `SecurityFilterChain` from `HttpSecurity` class.
  ```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.build();
    }
}
 ```
4. Inside this bean, we can define our filterchain object and return it.

See Code for some starter stuffs:
```agsl
@Configuration
@EnableWebSecurity//this will prevent default flow of spring security  filter chains and will make this as our default flow
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer-> customizer.disable());//we are disabling the csrf
        http.authorizeHttpRequests(request->request.anyRequest().authenticated());//All request should be authenticated
	    http.formLogin(Customizer.withDefaults());//it will enable the login form that we see by default with spring security
        http.httpBasic(Customizer.withDefaults());//this will allow basic authentication through Postman        
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//Make application stateless so that CSRF can be handled. //while using this disable http.formlogin
        //otherwise it will keep asking for username and password again and again as each resource is a new resource and it will act as a new session everytime
        return http.build();

        /*
        * Another way of writing upper code: Buildup pattern: one object is passed to multiple fns one by one
        * return http
        *           .csrf(Customizer-> Customizer.disable())
        *           .authorizeHttpRequests(request->request.anyRequest().authenticated())
        *           .httpBasic(Customizer.withDefaults())
        *           .sessionManagement(session->
        *                       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        *           .build();
        * */
    }
}
```

### How User Authentication is done with `UserDetailsService` class :
By default, the spring security authenticate the hardcode username and password. Now, we can customize that.
For example, we can allow multiple users to authenticate with username and password.

1. For this, we can create a `@Bean` annotated method, that returns an object of type `UserDetailsService`.
2. Now, we can't directly return an object of `UserDetailsService` as it is an interface. Either, we can implement it by creating a class
or we can return an object of a class that has already implemented this interface. The class implementing this `UserDetailsService` interface
is `InMemoryUserDetailsManager`
3. The constructor `InMemoryUserDetailsManager` takes, object/s of `UserDetails` types as
input argument.
4. This `UserDetails` is an interface so we can use object of `User` type as this class implements the `UserDetails` interface.

See Code:
```agsl
@Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1=User
                .withDefaultPasswordEncoder()
                .username("sachin")
                .password("s@123")
                .roles("USER")
                .build();
        UserDetails user2=User
                .withDefaultPasswordEncoder()
                .username("harsh")
                .password("h@123")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1,user2);
    }
```