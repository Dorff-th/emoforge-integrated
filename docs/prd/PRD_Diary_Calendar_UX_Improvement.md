# PRD — Diary Calendar Emotion Background

## Goal

Improve emotion visibility in the calendar by applying background colors to each day cell based on the average emotion score.

---

## Emotion Scale

The diary uses a 5-level emotion scale:

1 - Very Bad 😟  
2 - Bad 😕  
3 - Neutral 🙂  
4 - Good 😄  
5 - Very Good 🤩  

---

## Background Color Mapping

Apply background color to the DayCell based on the average emotion score of that day.

emotion = 1 → bg-red-50  
emotion = 2 → bg-orange-50  
emotion = 3 → bg-gray-50  
emotion = 4 → bg-yellow-50  
emotion = 5 → bg-pink-50  

The emoji indicator should remain visible in the cell.

---

## Behavior

If multiple diary entries exist on the same day:

averageEmotion = average(entry.emotion)

Use the rounded average value to determine the background color.

Example:

entries: [4,5,4]  
average = 4.33  
rounded = 4  
→ bg-yellow-50

---

## Files to Modify

emoforge-integrated-frontend/src/features/calendar/components/Calendar.tsx  
emoforge-integrated-frontend/src/features/calendar/components/CalendarDayCell.tsx  

---

## Constraints

- Do not modify existing emoji rendering
- Do not change modal behavior
- Only enhance visual background color