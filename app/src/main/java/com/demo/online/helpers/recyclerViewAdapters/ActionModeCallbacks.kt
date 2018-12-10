package com.demo.online.helpers.recyclerViewAdapters

import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem

interface ActionModeCallbacks:ActionMode.Callback {
    override fun onCreateActionMode(mode: ActionMode, menu: Menu):Boolean;

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu):Boolean;

    override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem):Boolean;

    override fun onDestroyActionMode(mode: ActionMode);
}