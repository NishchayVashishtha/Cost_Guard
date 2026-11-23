"""Add indexes on created_at columns for high-speed dashboard queries.

Revision ID: 0003
Revises: 0002
"""

from alembic import op
import sqlalchemy as sa

revision = "0003"
down_revision = "0002"
branch_labels = None
depends_on = None


def upgrade():
    op.create_index("ix_artifacts_created_at", "artifacts", ["created_at"])
    op.create_index("ix_comparisons_created_at", "comparisons", ["created_at"])
    op.create_index("ix_jobs_created_at", "jobs", ["created_at"])
    op.create_index("ix_investigations_created_at", "investigations", ["created_at"])


def downgrade():
    op.drop_index("ix_investigations_created_at", table_name="investigations")
    op.drop_index("ix_jobs_created_at", table_name="jobs")
    op.drop_index("ix_comparisons_created_at", table_name="comparisons")
    op.drop_index("ix_artifacts_created_at", table_name="artifacts")
