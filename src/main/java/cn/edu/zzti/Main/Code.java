package cn.edu.zzti.Main;

/**
 * Created by ae-mp02 on 2015/8/6.
 */
public class Code {
    private long id;
    private String real_char;
    private String binary_code;
    private int width;
    private int height;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReal_char() {
        return real_char;
    }

    public void setReal_char(String real_char) {
        this.real_char = real_char;
    }

    public String getBinary_code() {
        return binary_code;
    }

    public void setBinary_code(String binary_code) {
        this.binary_code = binary_code;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code = (Code) o;

        if (id != code.id) return false;
        if (real_char != null ? !real_char.equals(code.real_char) : code.real_char != null) return false;
        return !(binary_code != null ? !binary_code.equals(code.binary_code) : code.binary_code != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (real_char != null ? real_char.hashCode() : 0);
        result = 31 * result + (binary_code != null ? binary_code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", real_char='" + real_char + '\'' +
                ", binary_code='" + binary_code + '\'' +
                '}';
    }
}
