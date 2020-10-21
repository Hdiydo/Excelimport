package org.zxiat.entity.target;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Claim extends Snak implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date mod;

    private int cfd = 100;

    private HashMap<String, List<Snak>> qlfs;

    private List<Snak> refs;

    public Claim() {
        super();
    }

    public Claim(Object value) {
        super(value);
    }

    public Claim(Type type, Object value) {
        super(type, value);
    }

    public Claim(Snak snak) {
        this(snak.getTp(), snak.getVal());
    }

    public Date getMod() {
        return mod;
    }

    public void setMod(Date mod) {
        this.mod = mod;
    }

    public int getCfd() {
        return cfd;
    }

    public void setCfd(int cfd) {
        this.cfd = cfd;
    }

    public HashMap<String, List<Snak>> getQlfs() {
        return qlfs;
    }

    public void setQlfs(HashMap<String, List<Snak>> qlfs) {
        this.qlfs = qlfs;
    }

    public List<Snak> getRefs() {
        return refs;
    }

    public void setRefs(List<Snak> refs) {
        this.refs = refs;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "mod=" + mod +
                ", cfd=" + cfd +
                ", qlfs=" + qlfs +
                ", refs=" + refs +
                '}';
    }
}
