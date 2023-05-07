import { Container, Grid } from '@material-ui/core';
import React from 'react';
import { useSelector } from 'react-redux';
import { Outlet } from 'react-router-dom';

import { BottomLine } from '../components/BottomLine/BottomLine';
import { CustomSnackbar } from '../components/CustomSnackbar/CustomSnackbar';
import SideMenu from '../components/SideMenu/SideMenu';
// import { CustomSnackbar } from '../components/CustomSnackbar/CustomSnackbar';
import { handleSnackbar } from '../features/slices/snackbarSlice';
import { useLayoutStyles } from './LayoutStyles';

export const Layout = () => {
  const classes = useLayoutStyles();
  const { user } = useSelector((state) => state.auth);

  return (
    <>
      <Container className={classes.wrapper} maxWidth="lg">
        <Grid sm={1} md={2} item style={{ minWidth: '256px' }}>
          <SideMenu />
        </Grid>
        <Grid container spacing={3}>
          <Outlet />
        </Grid>
      </Container>
      {!user && <BottomLine />}
    </>
  );
};
