package olimp

enum class OlimpMessageType(override var value: Char?): ValueRepresentable<Char?> {
    OPEN('o'),
    UPDATE('a'),
    PING('h');
}



