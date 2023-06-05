import {makeStyles} from "@mui/styles";

export const useMessagesModalStyles = makeStyles((theme) => ({

    header: {
        margin: 0,
        backgroundColor: theme.palette.background.default,
        border: 0,
        display: "flex",
        justifyContent:"space-between",

        "& svg": {
            fontSize: 26,
        },
    },
    headerMessage: {
        marginLeft: 0,

    },
    button: {
        padding: 12,
        height: 30,
    },
    content: {
        height: 550,
        width: 598,
        padding: 0,
        borderRadius: 16,
    },
    divider: {
        height: 1,
        backgroundColor: theme.palette.background.lightDefault,
    },
}));
