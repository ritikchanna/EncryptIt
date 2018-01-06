package ritik.encryptit;

/**
 * Created by SuperUser on 02-01-2018.
 */

public class config {
    private Boolean savable;
    private int burn_time;
    private double valid_time;
    private int password_type;
    private String format_orignal;

    config() {

    }

    config(Boolean savable, int burn_time, double valid_time, int password_type, String format_orignal) {
        this.savable = savable;
        this.burn_time = burn_time;
        this.valid_time = valid_time;
        this.password_type = password_type;
        this.format_orignal = format_orignal;
    }

    public Boolean getSavable() {
        return savable;
    }

    public void setSavable(Boolean savable) {
        this.savable = savable;
    }

    public int getBurn_time() {
        return burn_time;
    }

    public void setBurn_time(int burn_time) {
        this.burn_time = burn_time;
    }

    public double getValid_time() {
        return valid_time;
    }

    public void setValid_time(double valid_time) {
        this.valid_time = valid_time;
    }

    public int getPassword_type() {
        return password_type;
    }

    public void setPassword_type(int password_type) {
        this.password_type = password_type;
    }

    public String getFormat_orignal() {
        return format_orignal;
    }

    public void setFormat_orignal(String format_orignal) {
        this.format_orignal = format_orignal;
    }
}
