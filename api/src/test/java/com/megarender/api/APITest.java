package com.demo.api;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Midas on 19.02.2018.
 */

public class APITest {

    private User user;

    private API api;

    @Before
    public void AuthorizationAStt() throws IOException {
        API _api = new API();
        user = _api.TryLoginUser("tt","321tt");
        api = new API(user.Id,user.Hash);
    }

    @Test
    public void TTSceneLoadSuccess() throws IOException {
        ArrayList<Scene> scenes = api.LoadScenes();
        assertEquals(2,scenes.size());
        assertEquals(4,scenes.get(1).Tasks.size());
    }

    @Test
    public void TTArchiveLoadSuccess() throws IOException {
        ArrayList<Archive> archives = api.LoadArchives();
        assertEquals(34,archives.size());
    }

    @Test
    public void TTLoadUser() throws IOException {
        User _user = api.LoadUser();
        assertEquals(user.Id,_user.Id);
    }
    @Test
    public void TTGetTaskSettings() throws IOException {
        RenderSettings rs = api.GetTaskSettings(200999);
        assertEquals(rs.Name,"please_not_remove");
    }
}

