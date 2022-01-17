import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from './home/home.component';
import { SiteComponent } from './site/site.component';

const routes: Routes = [
  { path: '', component: SiteComponent },
  { path: 'painel', component: LayoutComponent, children:[
    { path: 'home', component: HomeComponent }
    
  ]}

]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
