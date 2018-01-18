package zoro.test.com.functionset.phonephotoshow;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : Zoro.
 * @Date : 2017/9/21.
 * @Describe :
 */

public class EB_DetailPhoneToPhonePhoto implements Serializable{
    private List<String> list;

    public EB_DetailPhoneToPhonePhoto(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "EB_DetailPhoneToPhonePhoto{" +
                "list=" + list +
                '}';
    }
}
