### 手動で設定（なぜかうまくいかない）

fcitx5 --disable=wayland,waylandim -d

fcitx5-configtool

### 日本語の切り替えコマンドは下記のいずれかをする。

- Ctrl + Space
- Super（Windows キー）+ Space

### データベースの設定

java で sampledb としてテーブルにアクセスしているため、sampledb のデータベースを作成。

それ以外のことは以下の URL 参照

[データベースの設定](https://github.com/hyk4869/studySQL)

### メモ(無視)

sudo service dbus status

sudo service dbus start

mkdir -p ~/.config/fcitx5
nano ~/.config/fcitx5/profile

[AddonList]
Disable=wayland,waylandim
