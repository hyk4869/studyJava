#!/bin/bash

# Start dbus if it's not running
if ! pgrep -x "dbus-daemon" > /dev/null
then
    echo "Starting dbus..."
    sudo service dbus start
    sleep 2  # Wait a bit for dbus to start properly
fi

# Check if dbus has started correctly
if ! pgrep -x "dbus-daemon" > /dev/null
then
    echo "Failed to start dbus. Please check the dbus installation and configuration."
    exit 1
fi

# Set environment variables for fcitx5
export DISPLAY=:1
export GTK_IM_MODULE=fcitx
export QT_IM_MODULE=fcitx
export XMODIFIERS=@im=fcitx
export FCITX_DISABLE_WAYLAND=1

# Start a virtual framebuffer (Xvfb) if it's not running as root
if ! pgrep -x "Xvfb" > /dev/null
then
    echo "Starting Xvfb as root..."
    sudo Xvfb :1 -screen 0 1024x768x16 &
    sleep 2  # Wait a bit for Xvfb to start properly
fi

# Confirm that DISPLAY is set
if [ -z "$DISPLAY" ]; then
    echo "Failed to set DISPLAY environment variable."
    exit 1
fi

# Create ~/.config/fcitx5/profile if it doesn't exist and disable wayland modules
mkdir -p ~/.config/fcitx5
PROFILE_FILE=~/.config/fcitx5/profile

if [ ! -f "$PROFILE_FILE" ]; then
    echo "Creating profile file for fcitx5..."
    cat <<EOL > "$PROFILE_FILE"
[Groups/0]
# Group Name
Name=デフォルト
# Layout
Default Layout=us
# Default Input Method
DefaultIM=mozc

[Groups/0/Items/0]
# Name
Name=keyboard-us
# Layout
Layout=

[Groups/0/Items/1]
# Name
Name=mozc
# Layout
Layout=

[GroupOrder]
0=デフォルト

[AddonList]
Disable=wayland,waylandim
EOL
    echo "Profile file created and wayland disabled."
else
    echo "Profile file already exists."
fi

# Start fcitx5 with wayland and waylandim disabled
echo "Starting fcitx5 with wayland and waylandim disabled..."
fcitx5 --disable=wayland,waylandim -d &
