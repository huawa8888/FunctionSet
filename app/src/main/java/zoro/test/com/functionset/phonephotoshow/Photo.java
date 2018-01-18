package zoro.test.com.functionset.phonephotoshow;

import java.io.Serializable;

/**
 * @Author : Zoro.
 * @Date : 2017/9/19.
 * @Describe :照片实体
 */

public class Photo implements Serializable {

    private int id;
    private String path;  //路径
    private Boolean checked;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Photo(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }
}

