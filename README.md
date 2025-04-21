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