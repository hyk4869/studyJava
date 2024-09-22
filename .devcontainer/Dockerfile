FROM mcr.microsoft.com/devcontainers/base:debian

# Install locales, timezone, Japanese fonts, and fcitx5-mozc
RUN apt-get update && apt-get install -y \
    bash \
    locales \
    tzdata \
    x11-apps \
    libxtst6 \
    libfreetype6 \
    fonts-noto-cjk \
    fontconfig \
    fcitx5 \
    fcitx5-mozc \
    && echo "ja_JP.UTF-8 UTF-8" > /etc/locale.gen \
    && locale-gen ja_JP.UTF-8 \
    && update-locale LANG=ja_JP.UTF-8 \
    && cp /usr/share/zoneinfo/Asia/Tokyo /etc/localtime \
    && echo "Asia/Tokyo" > /etc/timezone \
    && apt-get clean \
    && fc-cache -fv

# Set environment variables for fcitx5
ENV LANG=ja_JP.UTF-8
ENV LANGUAGE=ja_JP:ja
ENV LC_ALL=ja_JP.UTF-8
ENV GTK_IM_MODULE=fcitx
ENV QT_IM_MODULE=fcitx
ENV XMODIFIERS=@im=fcitx
ENV NODE_OPTIONS="--max-old-space-size=8192"
ENV TZ=Asia/Tokyo

# Add ll alias
RUN echo alias ll=\'ls -laF --color=auto\' >> /home/vscode/.bashrc
