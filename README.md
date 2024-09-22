### 日本語の切り替えコマンドは下記のいずれかをする。

- Ctrl + Space
- Super（Windows キー）+ Space

### 手動で設定（なぜかうまくいかない）

sudo service dbus status
sudo service dbus start

fcitx5 --disable=wayland,waylandim -d
fcitx5-configtool

mkdir -p ~/.config/fcitx5
nano ~/.config/fcitx5/profile

[AddonList]
Disable=wayland,waylandim
