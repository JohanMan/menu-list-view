# menu-list-view
类似于侧滑ListView，但是不是滑动打开菜单，是点击打开菜单

![效果图](https://github.com/JohanMan/menu-list-view/raw/master/src/main/res/drawable/Screenshot.png)

## 使用方法
（1）在xml生命MenuListView
```
<com.johan.menulistview.MenuListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:dividerHeight="10dp"
        android:divider="@android:color/transparent"
        android:listSelector="@android:color/transparent"
        />
```
（2）ListView的Item布局文件
```
<?xml version="1.0" encoding="utf-8"?>
<com.johan.menulistview.MenuListViewItemLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <ViewGroup>
    # ViewGroup 代表 LinearLayout RelativeLayout FrameLayout ...
    </ViewGroup>
</com.johan.menulistview.MenuListViewItemLayout>
```
（3）java代码
```
...
private static final int MENU_ID_EDIT = 1;
private static final int MENU_ID_DELETE = 2;
...
private ItemMenu createEditMenu() {
    ItemMenu editMenu = new ItemMenu(MENU_ID_EDIT);
    editMenu.setWidth(getResources().getDimensionPixelSize(R.dimen.check_account_item_option_width));
    editMenu.setBackground(R.color.account_item_edit_color);
    editMenu.setIcon(R.drawable.account_item_edit);
    editMenu.setIconSize(getResources().getDimensionPixelSize(R.dimen.check_account_item_option_icon_size));
    return editMenu;
}
private ItemMenu createDeleteMenu() {
    ItemMenu deleteMenu = new ItemMenu(MENU_ID_DELETE);
    deleteMenu.setWidth(getResources().getDimensionPixelSize(R.dimen.check_account_item_option_width));
    deleteMenu.setBackground(R.color.account_item_delete_color);
    deleteMenu.setIcon(R.drawable.account_item_delete);
    deleteMenu.setIconSize(getResources().getDimensionPixelSize(R.dimen.check_account_item_option_icon_size));
    return deleteMenu;
}
...
listView.addItemMenu(createEditMenu());
listView.addItemMenu(createDeleteMenu());
listView.setOnItemMenuClickListener(new MenuListView.OnItemMenuClickListener() {
    @Override
    public void onItemMenuClick(int id, int position) {
        switch (id) {
            case MENU_ID_EDIT :
                break;
            case MENU_ID_DELETE :
                break;
        }
    }
});
```
