# FloatMenu

A personal replacement for Samsung's Assistant menu: a floating button that opens a
menu of system shortcuts. Private by design: no internet permission, no analytics,
and the accessibility service can't read screen content.

## Run it

1. Open this folder in Android Studio and let Gradle sync (it may offer to install
   SDK Platform 37; accept).
2. Plug in the phone with USB debugging on, pick it in the device dropdown, press Run.
3. In the app, tap **Open Accessibility settings** and turn FloatMenu on.

## Build phases

- [x] 1. Skeleton: onboarding screen + empty accessibility service
- [x] 2. Floating button (draggable, tap shows a Toast)
- [ ] 3. Spring physics (edge snap, fling, press scale, idle fade)
- [ ] 4. Menu with all v1 actions + open/close animations
- [ ] 5. Settings screen wired to the overlay
- [ ] 6. Polish (theme, rotation, TalkBack, edge cases)
