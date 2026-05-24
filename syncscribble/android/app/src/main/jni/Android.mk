#include $(call all-subdir-makefiles)

# Note that symlinking source dirs is a terrible idea which can create a huge mess when trying to open files,
#  esp. when debugging
include $(WRITE_HOME)/SDL/Android.mk

# Define libmain explicitly instead of including the Makefile
include $(CLEAR_VARS)

LOCAL_MODULE := main

WRITE_SRC := $(WRITE_HOME)/syncscribble

LOCAL_C_INCLUDES := \
    $(WRITE_SRC) \
    $(WRITE_SRC)/.. \
    $(WRITE_HOME)/SDL/include \
    $(WRITE_HOME)/nanovgXC/src \
    $(WRITE_HOME)/nanovgXC/glad \
    $(WRITE_HOME)/pugixml/src \
    $(WRITE_HOME)/stb

LOCAL_CFLAGS := \
    -DPUGIXML_NO_XPATH \
    -DPUGIXML_NO_EXCEPTIONS \
    -DANDROID

LOCAL_CPPFLAGS := -std=c++14 -Wno-unused -Wno-error=format-security

LOCAL_SRC_FILES := \
    $(WRITE_SRC)/application.cpp \
    $(WRITE_SRC)/resources.cpp \
    $(WRITE_SRC)/basics.cpp \
    $(WRITE_SRC)/strokebuilder.cpp \
    $(WRITE_SRC)/element.cpp \
    $(WRITE_SRC)/page.cpp \
    $(WRITE_SRC)/syncundo.cpp \
    $(WRITE_SRC)/selection.cpp \
    $(WRITE_SRC)/document.cpp \
    $(WRITE_SRC)/scribblemode.cpp \
    $(WRITE_SRC)/scribbleinput.cpp \
    $(WRITE_SRC)/scribbleview.cpp \
    $(WRITE_SRC)/bookmarkview.cpp \
    $(WRITE_SRC)/clippingview.cpp \
    $(WRITE_SRC)/scribblearea.cpp \
    $(WRITE_SRC)/scribbledoc.cpp \
    $(WRITE_SRC)/scribblewidget.cpp \
    $(WRITE_SRC)/scribbleconfig.cpp \
    $(WRITE_SRC)/scribblesync.cpp \
    $(WRITE_SRC)/documentlist.cpp \
    $(WRITE_SRC)/rulingdialog.cpp \
    $(WRITE_SRC)/configdialog.cpp \
    $(WRITE_SRC)/linkdialog.cpp \
    $(WRITE_SRC)/pentoolbar.cpp \
    $(WRITE_SRC)/syncdialog.cpp \
    $(WRITE_SRC)/touchwidgets.cpp \
    $(WRITE_SRC)/mainwindow.cpp \
    $(WRITE_SRC)/scribbleapp.cpp \
    $(WRITE_SRC)/android/androidhelper.cpp \
    $(WRITE_HOME)/ugui/svggui.cpp \
    $(WRITE_HOME)/ugui/widgets.cpp \
    $(WRITE_HOME)/ugui/textedit.cpp \
    $(WRITE_HOME)/ugui/colorwidgets.cpp \
    $(WRITE_HOME)/ulib/geom.cpp \
    $(WRITE_HOME)/ulib/image.cpp \
    $(WRITE_HOME)/ulib/path2d.cpp \
    $(WRITE_HOME)/ulib/painter.cpp \
    $(WRITE_HOME)/usvg/svgnode.cpp \
    $(WRITE_HOME)/usvg/svgstyleparser.cpp \
    $(WRITE_HOME)/usvg/svgparser.cpp \
    $(WRITE_HOME)/usvg/svgpainter.cpp \
    $(WRITE_HOME)/usvg/svgwriter.cpp \
    $(WRITE_HOME)/usvg/pdfwriter.cpp \
    $(WRITE_HOME)/usvg/cssparser.cpp \
    $(WRITE_HOME)/nanovgXC/src/nanovg.c \
    $(WRITE_HOME)/pugixml/src/pugixml.cpp \
    $(WRITE_HOME)/miniz/miniz.c \
    $(WRITE_HOME)/miniz/miniz_tdef.c \
    $(WRITE_HOME)/miniz/miniz_tinfl.c

LOCAL_STATIC_LIBRARIES := SDL2main
LOCAL_SHARED_LIBRARIES := SDL2
LOCAL_LDLIBS := -lGLESv3 -llog -ljnigraphics -landroid

#$(call import-add-path, /Users/ozpineci/android_dev/write_apps/Write_github)
#$(call import-module, SDL)
include $(BUILD_SHARED_LIBRARY)

