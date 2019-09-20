### Attendees
To load attendee data from file,
make a copy of `attendees.csv.sample` and rename it into `attendees.csv`,
then fill it with your users' data in the same format
`First Name,Last Name,E-Mail`.

----
### Prizes
Same for `prizes.csv.sample`: it must be copied into `prizes.csv`, 
then filled with details in format `Number,Name,Description,Image,Secret`.

The fields should be as follows:
- `Number` is the order number (rows should be sorted from "worst" prize with the highest number,
  to the "best" prize with the lowest number - they will be distributed from worst to best);
- `Name` is a brief description of the prize;
- `Description` can be a longer description with more details or sponsor credits;
- `Image` should be the name of an image file in the same folder as this file;
- `Secret` is an optional field that won't be displayed but only reported in the logs
  (e.g. a promo code to redeem)
  
----
### Images
Any number of images can be used in `prizes.csv`, 
as long as they are copied in the same `resources` folder.

---
### Texts
Some texts displayed in the app can be configured in a `config.properties` file:
as for the other files, copy it from `config.properties.sample` and modify as needed.
