package com.yn.reader.model.booklist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunxy on 2018/3/16.
 */

public class NavigationCategory {

    private List<Navigation> word;
    private List<Navigation> status;
    private List<Navigation> chargetype;
    private List<Navigation> lastnevigate;
    private List<String> tags;
    private List<Navigation> navigationTags;

    public List<Navigation> getWord() {
        if (!containAll(word)) word.add(0, new Navigation("全部"));
        return word;
    }

    public void setWord(List<Navigation> word) {
        this.word = word;
    }

    public List<Navigation> getStatus() {
        if (!containAll(status)) status.add(0, new Navigation("全部"));
        return status;
    }

    public void setStatus(List<Navigation> status) {
        this.status = status;
    }

    public List<Navigation> getChargetype() {
        if (!containAll(chargetype)) chargetype.add(0, new Navigation("全部"));
        return chargetype;
    }

    public void setChargetype(List<Navigation> chargetype) {
        this.chargetype = chargetype;
    }

    public List<Navigation> getLastnevigate() {
        if (!containAll(lastnevigate)) lastnevigate.add(0, new Navigation("全部"));
        return lastnevigate;
    }

    public void setLastnevigate(List<Navigation> lastnevigate) {
        this.lastnevigate = lastnevigate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        if (tags != null) {
            navigationTags = new ArrayList<>(tags.size());
            for (String tag : tags) {
                if (tag != null) {
                    Navigation navigation = new Navigation();
                    navigation.setName(tag);
                    navigationTags.add(navigation);
                }
            }
        }
    }

    public List<Navigation> getNavigationTags() {
        if (!containAll(navigationTags)) navigationTags.add(0, new Navigation("全部"));
        return navigationTags;
    }

    private boolean containAll(List<Navigation> navigations) {
        boolean isContainAll = false;
        for (Navigation bean : navigations) {
            if (bean.getName().equals("全部")) {
                isContainAll = true;
                break;
            }
        }
        return isContainAll;
    }
}
