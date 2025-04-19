package org.ibadalrahman.fp

/**
 * SafeLet is a way to combine a null checking and an if statements into one line.
 * The closure will have the input values but *non-null*
 */
inline fun <T0 : Any, R : Any>
        safeLet(p1: T0?, block: (T0) -> R?): R? =
    if (p1 == null) null
    else block(p1)

/**
 * SafeLet is a way to combine a null checking and an if statements into one line.
 * The closure will have the input values but *non-null*
 */
inline fun <T0 : Any, T1 : Any, R : Any>
        safeLet(p1: T0?, p2: T1?, block: (T0, T1) -> R?): R? =
    if (p1 == null) null
    else if (p2 == null) null
    else block(p1, p2)

/**
 * SafeLet is a way to combine a null checking and an if statements into one line.
 * The closure will have the input values but *non-null*
 */
inline fun <T0 : Any, T1 : Any, T2 : Any, R : Any>
        safeLet(p1: T0?, p2: T1?, p3: T2?, block: (T0, T1, T2) -> R?): R? =
    if (p1 == null) null
    else if (p2 == null) null
    else if (p3 == null) null
    else block(p1, p2, p3)

/**
 * SafeLet is a way to combine a null checking and an if statements into one line.
 * The closure will have the input values but *non-null*
 */
inline fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, R : Any>
        safeLet(p1: T0?, p2: T1?, p3: T2?, p4: T3?, block: (T0, T1, T2, T3) -> R?): R? =
    if (p1 == null) null
    else if (p2 == null) null
    else if (p3 == null) null
    else if (p4 == null) null
    else block(p1, p2, p3, p4)

/**
 * SafeLet is a way to combine a null checking and an if statements into one line.
 * The closure will have the input values but *non-null*
 */
inline fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any>
        safeLet(p1: T0?, p2: T1?, p3: T2?, p4: T3?, p5: T4?, block: (T0, T1, T2, T3, T4) -> R?): R? =
    if (p1 == null) null
    else if (p2 == null) null
    else if (p3 == null) null
    else if (p4 == null) null
    else if (p5 == null) null
    else block(p1, p2, p3, p4, p5)

/**
 * SafeLet is a way to combine a null checking and an if statements into one line.
 * The closure will have the input values but *non-null*
 */
inline fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any>
        safeLet(p1: T0?, p2: T1?, p3: T2?, p4: T3?, p5: T4?, p6: T5?, block: (T0, T1, T2, T3, T4, T5) -> R?): R? =
    if (p1 == null) null
    else if (p2 == null) null
    else if (p3 == null) null
    else if (p4 == null) null
    else if (p5 == null) null
    else if (p6 == null) null
    else block(p1, p2, p3, p4, p5, p6)
