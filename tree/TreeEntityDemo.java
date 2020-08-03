import java.util.ArrayList;
import java.util.List;

public class TreeEntityDemo {
@TreeElement(name = "id")
private String id;
@TreeElement(name = "pid")
private String pid;
private String name;
@TreeElement(name = "children")
private List<TreeEntityDemo> treeEntityDemos = new ArrayList<>();
    public List<TreeEntityDemo> getTreeEntityDemos() {
        return treeEntityDemos;
    }

    public void setTreeEntityDemos(List<TreeEntityDemo> treeEntityDemos) {
        this.treeEntityDemos = treeEntityDemos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
