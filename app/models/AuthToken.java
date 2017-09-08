package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Matias Cicilia on 07-Sep-17.
 */
/* AuthToken: Es el token de AUTORIZACION (No autenticación) que devuelve Facebook al iniciar sesión.
 * Es util guardarlo para en el futuro poder realizar pedidos a la API de Facebook tales como
  * compartir posts. No confundir con Token de autenticacion (JWT) */
@Entity
public class AuthToken extends BaseModel {

    @Column(name = "TOKEN", unique = true, nullable = false)
    private String token;

    @Column(name = "DATE")
    private long date;

    @Column(name = "USERID", nullable = false)
    private long userId;

    @Column(name = "VALID")
    private boolean valid;

    public AuthToken() {
    }

    public AuthToken(String token, long date, long id) {
        this.token = token;
        this.date = date;
        this.userId = id;
        this.valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setId(long id) {
        this.userId = id;
    }
}

