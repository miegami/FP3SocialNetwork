import { isThisWeek, isToday, isYesterday } from "date-fns";
import moment from "moment";

export const formatChatMessageDate = (dateString) => {
    const dateTime = moment(dateString, "YYYY-MM-DD HH:mm:ss.SS");

    if (!dateTime.isValid()) {
        return "";
    }
    const currentDate = moment();

    const formattedDate = dateTime.format("MMM D");
    const formattedTime = dateTime.format("h:mm A");

    if (isToday(dateTime)) {
        return formattedTime;
    } else if (isYesterday(dateTime)) {
        return `Yesterday at ${formattedTime}`;
    } else if (isThisWeek(dateTime)) {
        return dateTime.format("ddd") + " " + formattedTime;
    } else if (dateTime.year() === currentDate.year()) {
        return formattedDate + ", " + formattedTime;
    } else {
        return formattedDate + " " + dateTime.year() + ", " + formattedTime;
    }
};

