import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-feedback-state',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (visible) {
      <section class="feedback" [class.error]="type === 'error'" [class.success]="type === 'success'">
        {{ message }}
      </section>
    }
  `,
})
export class FeedbackStateComponent {
  @Input() type: 'error' | 'success' | 'loading' | 'empty' = 'success';
  @Input() message = '';
  @Input() visible = false;
}
