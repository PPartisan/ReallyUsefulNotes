package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

public final class AsciiEmojis {

    public static final String[] EMOJIS = new String[] {
        "＿φ( °-°)/", "＿φ(°-°=)", "φ(．．;)", "φ(^∇^ )", "φ(◎◎ヘ)", "ψ(。。)"
    };

    private AsciiEmojis() {throw new AssertionError(); }

    public static String getRandomEmoji() {
        int index = (int)(Math.random()*EMOJIS.length);
        if (index > (EMOJIS.length - 1)) index = EMOJIS.length - 1;
        return EMOJIS[index];
    }
}
