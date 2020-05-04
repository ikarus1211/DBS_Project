package database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "player")
@Table(name = "player")
public class ORMplayer implements Serializable {
    @Id
    @Column(name = "player_id", unique = true)
    private int p_id;

    @Column(name = "player_name")
    private String pName;

    @Column(name = "player_password")
    private String pPassword;

    @Column(name = "email")
    private String email;

    @Column(name = "no_characters")
    private String no_char;

    @Column(name = "premium_points")
    private int pp;

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpPassword() {
        return pPassword;
    }

    public void setpPassword(String pPassword) {
        this.pPassword = pPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_char() {
        return no_char;
    }

    public void setNo_char(String no_char) {
        this.no_char = no_char;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }
}
