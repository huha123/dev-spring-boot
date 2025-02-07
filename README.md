## dependency
- spring boot
- spring security
- spring jpa
- h2 test


## security role, auth

### role
- role : ROLE_ADMIN > ROLE_USER > ROLE_TEMPORARY_USER > ROLE_GUEST

### auth
- auth : ROLE_ADMIN (AUTH_READ, AUTH_WRITE, AUTH_UPDATE, AUTH_DELETE)
- auth : ROLE_USER (AUTH_READ, AUTH_WRITE, AUTH_UPDATE)
- auth : ROLE_TEMPORARY_USER (AUTH_READ, AUTH_WRITE)
- auth : ROLE_GUEST (AUTH_READ)

