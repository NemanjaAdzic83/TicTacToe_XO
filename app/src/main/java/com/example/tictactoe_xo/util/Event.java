package com.example.tictactoe_xo.util;

/**
 * Wraps a one-time event (play a sound, animate a move) sent through LiveData.
 *
 * LiveData always re-delivers its last value to a new observer, e.g. after rotation.
 * That is right for state (whose turn it is, the score), but wrong for events, which
 * must be handled only once. The observer calls getContentIfNotHandled(): the first
 * call returns the content, every later call returns null.
 */
public class Event<T> {

    private final T content;
    private boolean handled = false;

    public Event(T content) {
        this.content = content;
    }

    /** Returns the content once; later calls return null. */
    public T getContentIfNotHandled() {
        if (handled) return null;
        handled = true;
        return content;
    }
}
