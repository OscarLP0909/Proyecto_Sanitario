import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HeaderComponent } from '../header/header.component';
import { ToastComponent } from '../toast/toast.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, HeaderComponent, ToastComponent],
  template: `
    <div class="flex h-screen overflow-hidden bg-gray-50 dark:bg-gray-950">
      <app-sidebar />
      <div class="flex flex-col flex-1 overflow-hidden">
        <app-header />
        <main class="flex-1 overflow-y-auto p-6 scrollbar-thin">
          <router-outlet />
        </main>
      </div>
      <app-toast />
    </div>
  `,
})
export class LayoutComponent {}
